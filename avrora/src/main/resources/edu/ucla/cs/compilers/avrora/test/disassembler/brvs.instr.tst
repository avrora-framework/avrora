; @Harness: disassembler
; @Result: PASS
  section .text  size=0x00000100 vma=0x00000000 lma=0x00000000 offset=0x00000034 ;2**0 
  section .data  size=0x00000000 vma=0x00000000 lma=0x00000000 offset=0x00000134 ;2**0 

start .text:

label 0x00000000  ".text":
      0x0: 0xfb 0xf1  brvs  .+126  ;  0x80
      0x2: 0xf3 0xf1  brvs  .+124  ;  0x80
      0x4: 0xeb 0xf1  brvs  .+122  ;  0x80
      0x6: 0xe3 0xf1  brvs  .+120  ;  0x80
      0x8: 0xdb 0xf1  brvs  .+118  ;  0x80
      0xa: 0xd3 0xf1  brvs  .+116  ;  0x80
      0xc: 0xcb 0xf1  brvs  .+114  ;  0x80
      0xe: 0xc3 0xf1  brvs  .+112  ;  0x80
     0x10: 0xbb 0xf1  brvs  .+110  ;  0x80
     0x12: 0xb3 0xf1  brvs  .+108  ;  0x80
     0x14: 0xab 0xf1  brvs  .+106  ;  0x80
     0x16: 0xa3 0xf1  brvs  .+104  ;  0x80
     0x18: 0x9b 0xf1  brvs  .+102  ;  0x80
     0x1a: 0x93 0xf1  brvs  .+100  ;  0x80
     0x1c: 0x8b 0xf1  brvs  .+98  ;  0x80
     0x1e: 0x83 0xf1  brvs  .+96  ;  0x80
     0x20: 0x7b 0xf1  brvs  .+94  ;  0x80
     0x22: 0x73 0xf1  brvs  .+92  ;  0x80
     0x24: 0x6b 0xf1  brvs  .+90  ;  0x80
     0x26: 0x63 0xf1  brvs  .+88  ;  0x80
     0x28: 0x5b 0xf1  brvs  .+86  ;  0x80
     0x2a: 0x53 0xf1  brvs  .+84  ;  0x80
     0x2c: 0x4b 0xf1  brvs  .+82  ;  0x80
     0x2e: 0x43 0xf1  brvs  .+80  ;  0x80
     0x30: 0x3b 0xf1  brvs  .+78  ;  0x80
     0x32: 0x33 0xf1  brvs  .+76  ;  0x80
     0x34: 0x2b 0xf1  brvs  .+74  ;  0x80
     0x36: 0x23 0xf1  brvs  .+72  ;  0x80
     0x38: 0x1b 0xf1  brvs  .+70  ;  0x80
     0x3a: 0x13 0xf1  brvs  .+68  ;  0x80
     0x3c: 0x0b 0xf1  brvs  .+66  ;  0x80
     0x3e: 0x03 0xf1  brvs  .+64  ;  0x80
     0x40: 0xfb 0xf0  brvs  .+62  ;  0x80
     0x42: 0xf3 0xf0  brvs  .+60  ;  0x80
     0x44: 0xeb 0xf0  brvs  .+58  ;  0x80
     0x46: 0xe3 0xf0  brvs  .+56  ;  0x80
     0x48: 0xdb 0xf0  brvs  .+54  ;  0x80
     0x4a: 0xd3 0xf0  brvs  .+52  ;  0x80
     0x4c: 0xcb 0xf0  brvs  .+50  ;  0x80
     0x4e: 0xc3 0xf0  brvs  .+48  ;  0x80
     0x50: 0xbb 0xf0  brvs  .+46  ;  0x80
     0x52: 0xb3 0xf0  brvs  .+44  ;  0x80
     0x54: 0xab 0xf0  brvs  .+42  ;  0x80
     0x56: 0xa3 0xf0  brvs  .+40  ;  0x80
     0x58: 0x9b 0xf0  brvs  .+38  ;  0x80
     0x5a: 0x93 0xf0  brvs  .+36  ;  0x80
     0x5c: 0x8b 0xf0  brvs  .+34  ;  0x80
     0x5e: 0x83 0xf0  brvs  .+32  ;  0x80
     0x60: 0x7b 0xf0  brvs  .+30  ;  0x80
     0x62: 0x73 0xf0  brvs  .+28  ;  0x80
     0x64: 0x6b 0xf0  brvs  .+26  ;  0x80
     0x66: 0x63 0xf0  brvs  .+24  ;  0x80
     0x68: 0x5b 0xf0  brvs  .+22  ;  0x80
     0x6a: 0x53 0xf0  brvs  .+20  ;  0x80
     0x6c: 0x4b 0xf0  brvs  .+18  ;  0x80
     0x6e: 0x43 0xf0  brvs  .+16  ;  0x80
     0x70: 0x3b 0xf0  brvs  .+14  ;  0x80
     0x72: 0x33 0xf0  brvs  .+12  ;  0x80
     0x74: 0x2b 0xf0  brvs  .+10  ;  0x80
     0x76: 0x23 0xf0  brvs  .+8  ;  0x80
     0x78: 0x1b 0xf0  brvs  .+6  ;  0x80
     0x7a: 0x13 0xf0  brvs  .+4  ;  0x80
     0x7c: 0x0b 0xf0  brvs  .+2  ;  0x80
     0x7e: 0x03 0xf0  brvs  .+0  ;  0x80
     0x80: 0xfb 0xf3  brvs  .-2  ;  0x80
     0x82: 0xf3 0xf3  brvs  .-4  ;  0x80
     0x84: 0xeb 0xf3  brvs  .-6  ;  0x80
     0x86: 0xe3 0xf3  brvs  .-8  ;  0x80
     0x88: 0xdb 0xf3  brvs  .-10  ;  0x80
     0x8a: 0xd3 0xf3  brvs  .-12  ;  0x80
     0x8c: 0xcb 0xf3  brvs  .-14  ;  0x80
     0x8e: 0xc3 0xf3  brvs  .-16  ;  0x80
     0x90: 0xbb 0xf3  brvs  .-18  ;  0x80
     0x92: 0xb3 0xf3  brvs  .-20  ;  0x80
     0x94: 0xab 0xf3  brvs  .-22  ;  0x80
     0x96: 0xa3 0xf3  brvs  .-24  ;  0x80
     0x98: 0x9b 0xf3  brvs  .-26  ;  0x80
     0x9a: 0x93 0xf3  brvs  .-28  ;  0x80
     0x9c: 0x8b 0xf3  brvs  .-30  ;  0x80
     0x9e: 0x83 0xf3  brvs  .-32  ;  0x80
     0xa0: 0x7b 0xf3  brvs  .-34  ;  0x80
     0xa2: 0x73 0xf3  brvs  .-36  ;  0x80
     0xa4: 0x6b 0xf3  brvs  .-38  ;  0x80
     0xa6: 0x63 0xf3  brvs  .-40  ;  0x80
     0xa8: 0x5b 0xf3  brvs  .-42  ;  0x80
     0xaa: 0x53 0xf3  brvs  .-44  ;  0x80
     0xac: 0x4b 0xf3  brvs  .-46  ;  0x80
     0xae: 0x43 0xf3  brvs  .-48  ;  0x80
     0xb0: 0x3b 0xf3  brvs  .-50  ;  0x80
     0xb2: 0x33 0xf3  brvs  .-52  ;  0x80
     0xb4: 0x2b 0xf3  brvs  .-54  ;  0x80
     0xb6: 0x23 0xf3  brvs  .-56  ;  0x80
     0xb8: 0x1b 0xf3  brvs  .-58  ;  0x80
     0xba: 0x13 0xf3  brvs  .-60  ;  0x80
     0xbc: 0x0b 0xf3  brvs  .-62  ;  0x80
     0xbe: 0x03 0xf3  brvs  .-64  ;  0x80
     0xc0: 0xfb 0xf2  brvs  .-66  ;  0x80
     0xc2: 0xf3 0xf2  brvs  .-68  ;  0x80
     0xc4: 0xeb 0xf2  brvs  .-70  ;  0x80
     0xc6: 0xe3 0xf2  brvs  .-72  ;  0x80
     0xc8: 0xdb 0xf2  brvs  .-74  ;  0x80
     0xca: 0xd3 0xf2  brvs  .-76  ;  0x80
     0xcc: 0xcb 0xf2  brvs  .-78  ;  0x80
     0xce: 0xc3 0xf2  brvs  .-80  ;  0x80
     0xd0: 0xbb 0xf2  brvs  .-82  ;  0x80
     0xd2: 0xb3 0xf2  brvs  .-84  ;  0x80
     0xd4: 0xab 0xf2  brvs  .-86  ;  0x80
     0xd6: 0xa3 0xf2  brvs  .-88  ;  0x80
     0xd8: 0x9b 0xf2  brvs  .-90  ;  0x80
     0xda: 0x93 0xf2  brvs  .-92  ;  0x80
     0xdc: 0x8b 0xf2  brvs  .-94  ;  0x80
     0xde: 0x83 0xf2  brvs  .-96  ;  0x80
     0xe0: 0x7b 0xf2  brvs  .-98  ;  0x80
     0xe2: 0x73 0xf2  brvs  .-100  ;  0x80
     0xe4: 0x6b 0xf2  brvs  .-102  ;  0x80
     0xe6: 0x63 0xf2  brvs  .-104  ;  0x80
     0xe8: 0x5b 0xf2  brvs  .-106  ;  0x80
     0xea: 0x53 0xf2  brvs  .-108  ;  0x80
     0xec: 0x4b 0xf2  brvs  .-110  ;  0x80
     0xee: 0x43 0xf2  brvs  .-112  ;  0x80
     0xf0: 0x3b 0xf2  brvs  .-114  ;  0x80
     0xf2: 0x33 0xf2  brvs  .-116  ;  0x80
     0xf4: 0x2b 0xf2  brvs  .-118  ;  0x80
     0xf6: 0x23 0xf2  brvs  .-120  ;  0x80
     0xf8: 0x1b 0xf2  brvs  .-122  ;  0x80
     0xfa: 0x13 0xf2  brvs  .-124  ;  0x80
     0xfc: 0x0b 0xf2  brvs  .-126  ;  0x80
     0xfe: 0x03 0xf2  brvs  .-128  ;  0x80

start .data:
